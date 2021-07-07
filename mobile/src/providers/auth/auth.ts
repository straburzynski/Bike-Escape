import { Injectable } from '@angular/core';
import { Http } from '@angular/http';
import { Storage } from '@ionic/storage';
import { ToastController, Events } from 'ionic-angular';
import { SERVER_URL, CURRENT_USER_JSON, SERVER_TIMEOUT, CURRENT_USER_IMAGE } from '../../config';
import { GlobalProvider } from '../global/global';
import { TranslateService } from '@ngx-translate/core';
import { AuthHttp } from './auth.http';
import { ImageService } from '../image-service/image-service';
import { ReplaySubject } from 'rxjs';

@Injectable()
export class AuthProvider {

    authUser = new ReplaySubject<any>(1);

    constructor(private http: Http,
                private authHttp: AuthHttp,
                private storage: Storage,
                public global: GlobalProvider,
                public toastCtrl: ToastController,
                public translate: TranslateService,
                public imageService: ImageService,
                public events: Events) {
        this.authUser.subscribe(jwt => {
            this.global.loggedIn = !!jwt;
        });
    }

    checkLogin() {
        this.storage.get('jwt').then(jwt => {
            this.global.token = jwt;
            if (jwt) {  // && !this.jwtHelper.isTokenExpired(jwt)
                this.storage.get(CURRENT_USER_JSON).then((data) => {
                    this.global.currentUser = data;
                    this.authUser.next(jwt);
                    this.events.publish('loggedIn');
                });
                // don't need to check authentication for token expired time
                // this.authHttp.get(`${SERVER_URL}authenticate`)
                //     .subscribe(() => this.authUser.next(jwt),
                //         (err) => this.storage.remove('jwt').then(() => this.authUser.next(null)));

            } else {
                this.storage.remove('jwt').then(() => this.authUser.next(null));
            }
        });
    }

    public doLogin(user: any, success: Function, fail: Function) {
        this.http.post(`${SERVER_URL}/auth/login`, user)
            .timeout(SERVER_TIMEOUT)
            .subscribe(
                response => {
                    if (response.headers) {
                        const loggedInUser = response.json();
                        const token = response.headers.get('x-auth-token');
                        this.global.token = token;
                        this.handleJwtResponse(token);
                        this.storage.set(CURRENT_USER_JSON, loggedInUser);
                        this.global.currentUser = loggedInUser;
                        this.imageService.setUserImage(loggedInUser.id);
                        success();
                    }
                },
                error => {
                    this.handleError(error);
                    fail();
                }
            );
    }

    public doSendResetPin(email, success: Function, fail: Function) {
        this.http.post(`${SERVER_URL}/user/sendToken`, email)
            .timeout(SERVER_TIMEOUT)
            .subscribe(
                response => {
                    this.handleSuccess(response.json().message);
                    success();
                },
                error => {
                    this.handleError(error);
                    fail();
                }
            );
    }

    public doChangePasswordWithPin(password, success: Function, fail: Function) {
        this.http.post(`${SERVER_URL}/user/resetPassword`, password)
            .timeout(SERVER_TIMEOUT)
            .subscribe(
                response => {
                    this.handleSuccess(response.json().message);
                    success();
                },
                error => {
                    this.handleError(error);
                    fail();
                }
            );
    }

    public doEditUser(name, success: Function, fail: Function) {
        const userTO = {'firstName': name};
        this.authHttp.put(`${SERVER_URL}/user`, userTO)
            .timeout(SERVER_TIMEOUT)
            .subscribe(
                response => {
                    this.handleSuccess(response.json().message);
                    this.refreshUser();
                    success();
                },
                error => {
                    this.handleError(error);
                    fail();
                }
            );
    }

    logout() {
        this.storage.remove('jwt').then(() => this.authUser.next(null));
        this.storage.remove(CURRENT_USER_JSON);
        this.storage.remove(CURRENT_USER_IMAGE);
    }

    doSignup(user: any, success: Function, fail: Function) {
        this.http.post(`${SERVER_URL}/auth/signup`, user)
            .timeout(SERVER_TIMEOUT)
            .subscribe(response => {
                    const token = response.headers.get('x-auth-token');
                    this.global.token = token;
                    this.handleJwtResponse(token);
                    this.storage.set(CURRENT_USER_JSON, response.json());
                    this.global.currentUser = response.json();
                    this.handleSuccess('Sign up successful');
                    success();
                },
                error => {
                    this.handleError(error);
                    fail();
                });
    }

    public doRemoveUser() {
        this.authHttp.delete(SERVER_URL + '/user')
            .timeout(SERVER_TIMEOUT)
            .subscribe((res) => {
                this.handleSuccess('User removed successful');
                this.logout();
            }, error => {
                this.handleError(error);
            });
    }

    public refreshUser() {
        this.authHttp.get(SERVER_URL + '/user')
            .map(response => response.json())
            .timeout(SERVER_TIMEOUT)
            .subscribe(data => {
                    this.global.currentUser = data;
                    this.storage.set(CURRENT_USER_JSON, data);
                },
                () => {
                    this.global.showToast('Error refreshing user data');
                }
            );
    }

    private handleJwtResponse(jwt: string) {
        return this.storage.set('jwt', jwt)
            .then(() => this.authUser.next(jwt))
            .then(() => jwt);
    }

    private handleError(error: any) {
        let message = 'Connection error';
        const errorJson = error.json();
        if (errorJson.message) {
            message = this.translate.instant(errorJson.message);
        }
        const toast = this.toastCtrl.create({
            message: message,
            duration: 3000,
            position: 'bottom'
        });
        toast.present();
    }

    private handleSuccess(message) {
        const toast = this.toastCtrl.create({
            message: this.translate.instant(message),
            duration: 3000,
            position: 'bottom'
        });
        toast.present();
    }

}
