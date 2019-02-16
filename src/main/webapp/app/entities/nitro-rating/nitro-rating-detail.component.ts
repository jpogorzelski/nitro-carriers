import { RatingDetailComponent } from 'app/entities/rating';
import { Component, OnInit } from '@angular/core';
import { AccountService } from 'app/core';
import { ActivatedRoute } from '@angular/router';

@Component({
    selector: 'jhi-nitro-rating-detail',
    templateUrl: './nitro-rating-detail.component.html'
})
export class NitroRatingDetailComponent extends RatingDetailComponent implements OnInit {
    currentAccount: any;

    constructor(protected accountService: AccountService, protected activatedRoute: ActivatedRoute) {
        super(activatedRoute);
    }

    ngOnInit() {
        super.ngOnInit();
        this.accountService.identity().then(account => {
            this.currentAccount = account;
        });
    }
}
