import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { LoginModalService, AccountService, Account } from 'app/core';

@Component({
    selector: 'jhi-home',
    templateUrl: './home.component.html',
    styleUrls: ['home.scss']
})
export class HomeComponent implements OnInit {
    account: Account;
    modalRef: NgbModalRef;

    constructor(
        private accountService: AccountService,
        private loginModalService: LoginModalService,
        private eventManager: JhiEventManager,
        private router: Router
    ) {}

    ngOnInit() {
        console.log('init!');
        //   this.proceedWithUser();
        //    this.registerAuthenticationSuccess();
    }

    private proceedWithUser() {
        this.accountService.identity().then((account: Account) => {
            this.account = account;
            if (this.isAuthenticated()) {
                console.log('logged in!');
                this.router.navigate(['/nitro-rating']);
            }
        });
    }

    registerAuthenticationSuccess() {
        this.eventManager.subscribe('authenticationSuccess', () => {
            this.proceedWithUser();
        });
    }

    isAuthenticated() {
        return this.accountService.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }
}
