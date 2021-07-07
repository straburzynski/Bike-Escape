import { ConnectionBackend, Headers, Http, Request, RequestOptions, RequestOptionsArgs, Response } from '@angular/http';
import { Router } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { Injectable } from '@angular/core';
import { AuthService } from './auth.service';
import { TIMEOUT } from './config.service';

@Injectable()
export class AuthHttp extends Http {

  constructor(backend: ConnectionBackend,
              defaultOptions: RequestOptions,
              private router: Router,
              private authService: AuthService) {
    super(backend, defaultOptions);
  }

  request(url: string | Request, options?: RequestOptionsArgs): Observable<Response> {
    return this.intercept(super.request(url, options));
  }

  get(url: string, options?: RequestOptionsArgs): Observable<Response> {
    return this.intercept(super.get(url, this.getRequestOptionArgs(options)));
  }

  post(url: string, body: any, options?: RequestOptionsArgs): Observable<Response> {
    return this.intercept(super.post(url, body, this.getRequestOptionArgs(options)));
  }

  put(url: string, body: any, options?: RequestOptionsArgs): Observable<Response> {
    return this.intercept(super.put(url, body, this.getRequestOptionArgs(options)));
  }

  delete(url: string, options?: RequestOptionsArgs): Observable<Response> {
    return this.intercept(super.delete(url, this.getRequestOptionArgs(options)));
  }

  getRequestOptionArgs(options?: RequestOptionsArgs): RequestOptionsArgs {
    if (options == null) {
      options = new RequestOptions();
    }
    if (options.headers == null) {
      options.headers = new Headers();
    }
    options.headers.append('X-AUTH-TOKEN', this.authService.getSession().token);
    return options;
  }

  intercept(observable: Observable<Response>) {
    return observable.timeout(TIMEOUT).catch((error, source) => {
      if (error.status === 401) {
        this.router.navigate(['']);
        return Observable.throw(error);
      } else if (error.status === 403) {
        this.router.navigate(['']);
        return Observable.throw(error);
      } else {
        return Observable.throw(error);
      }
    });
  }

}
