import { RatingComponent } from 'app/entities/rating';
import { Component } from '@angular/core';

@Component({
    selector: 'jhi-nitro-rating',
    templateUrl: './nitro-rating.component.html'
})
export class NitroRatingComponent extends RatingComponent {
    login: any;

    ngOnInit() {
        super.ngOnInit();
        this.accountService.identity().then(account => {
            this.login = account.login;
        });
    }
}
