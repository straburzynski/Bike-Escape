import { Http, Request, RequestOptionsArgs, Response, RequestOptions, Headers, ConnectionBackend } from '@angular/http';
import 'rxjs/add/operator/timeout';
import { Injectable } from '@angular/core';
import { GlobalProvider } from '../global/global';
import { SERVER_TIMEOUT } from '../../config';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class AuthHttp extends Http {

    readonly ERROR_OFFLINE_TYPE: string = '3';
    private lastConnectionToastTime = 0;

    constructor(backend: ConnectionBackend,
                defaultOptions: RequestOptions,
                public global: GlobalProvider) {
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

        options.headers.append('X-AUTH-TOKEN', this.global.token);

        return options;
    }

    intercept(observable: Observable<Response>): Observable<Response> {
        return observable
            .timeout(SERVER_TIMEOUT)
            .catch((error, source) => {
                if (error.status === 401) {
                    return Observable.empty();
                } else {
                    this.processErrorMessage(error);
                    return Observable.throw(error);
                }
            });
    }

    processErrorMessage(error) {
        if (error.constructor.name === 'TimeoutError' || error.type === this.ERROR_OFFLINE_TYPE) {
            const time = new Date().getTime();
            if (time - this.lastConnectionToastTime > 3000) {
                this.global.showToast('No connection available');
                this.lastConnectionToastTime = time;
            }
        }
    }
}
