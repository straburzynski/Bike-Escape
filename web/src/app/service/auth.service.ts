import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
import 'rxjs/add/operator/retry';
import 'rxjs/add/operator/timeout';
import 'rxjs/add/operator/delay';
import 'rxjs/add/operator/map';
import { Router } from '@angular/router';
import { Subject } from 'rxjs/Subject';
import { SERVER_URL } from './config.service';

@Injectable()
export class AuthService {

  public currentUser: any = null;
  public isAdmin = false;
  public errorMessage: string = null;
  private authenticated = false;
  private token: string;
  private expires: any = 0;
  private expiresTimerId: any = null;
  private _isAuthenticated = new Subject<boolean>();

  constructor(private http: Http,
              private router: Router) {

    this.restoreSessionIfPossible();
    this._isAuthenticated.next(false);
    this.isAuthenticated(
      () => {
        // success
      },
      () => {
        // fail
      }
    );
  }

  public doLogin(email: string, password: string, success: Function, fail: Function) {
    this.http.post(SERVER_URL + '/auth/login', {
      'email': email,
      'password': password
    }).subscribe(
      response => {
        if (response.headers) {
          const token = response.headers.get('x-auth-token');
          this.loginSuccess(token, response.json(), success);
        }
      },
      error => {
        this.errorMessage = error.json().message;
        if (this.errorMessage == null) {
          this.errorMessage = 'Connection error';
        }
        this.loginFailure(fail);
      }
    );
  }

  public doLogout() {
    this.authenticated = false;
    this._isAuthenticated.next(false);
    this.expiresTimerId = null;
    this.expires = 0;
    this.token = null;
    this.currentUser = null;
    this.clearStorage();
    this.router.navigate(['/login']);
  }

  public getSession() {
    if (!this.token) {
      const tokenFromStorage = localStorage.getItem('token');

      if (tokenFromStorage) {
        this.authenticated = true;
        this.token = tokenFromStorage;
      }
    }

    return {
      authenticated: this.authenticated,
      token: this.token,
      expires: this.expires
    };
  }

  public isAuthenticated(success: Function, fail: Function) {
    if (!this.token) {
      const tokenFromStorage = localStorage.getItem('token');

      if (tokenFromStorage) {
        const headers = new Headers();
        headers.append('X-AUTH-TOKEN', tokenFromStorage);
        this.http.get(SERVER_URL + '/user', {headers: headers})
          .map(response => response.json())
          .subscribe((data: any) => {
              this.loginSuccess(tokenFromStorage, data, () => {
                // success
              });
            },
            error => {
              this.loginFailure(() => {
                // fail
              });
            },
            () => {
              // nothing
            });
      }
    } else {
      success();
    }
  }

  // checkIfUserIsAdmin() {
  //   if (this.token) {
  //     this.checkAdminPermissionRequest(this.token)
  //   }
  //   const tokenFromStorage = localStorage.getItem('token');
  //   if (tokenFromStorage) {
  //     this.checkAdminPermissionRequest(tokenFromStorage);
  //   } else {
  //     this.isAdmin = false;
  //   }
  // }
  //
  // private checkAdminPermissionRequest(token) {
  //   const headers = new Headers();
  //   headers.append('X-AUTH-TOKEN', token);
  //   this.http.get(SERVER_URL + '/user', {headers: headers})
  //     .map(res => res.json())
  //     .subscribe(data => {
  //         this.isAdmin = data.authorities.includes('ROLE_ADMIN');
  //       },
  //       () => {
  //         return false;
  //       })
  // }


  private loginSuccess(token: string, data: any, success: Function) {
    try {
      this.authenticated = true;
      this._isAuthenticated.next(true);
      localStorage.setItem('token', token);

      if (data) {
        localStorage.setItem('currentUser', JSON.stringify(data));
        this.currentUser = data;
        this.isAdmin = this.currentUser.authorities.includes('ROLE_ADMIN');
      }

      if (success) {
        success();
      }

    } catch (e) {
      this.loginFailure(() => {
        // fail
      });
    }
  }

  private loginFailure(fail: Function) {
    this.authenticated = false;
    this.currentUser = null;
    this._isAuthenticated.next(false);
    this.clearStorage();
    if (fail) {
      fail();
    }
  }

  private restoreSessionIfPossible() {
    const restoredUser = localStorage.getItem('currentUser');
    if (restoredUser != null) {
      this.currentUser = JSON.parse(restoredUser);
    }
  }

  private clearStorage() {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
  }

}
