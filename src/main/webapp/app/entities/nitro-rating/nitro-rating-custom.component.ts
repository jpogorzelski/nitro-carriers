import { Component, Input } from '@angular/core';
import { Grade } from 'app/shared/model/rating.model';
import { NitroRatingComponent } from 'app/entities/nitro-rating/nitro-rating.component';
import { ITEMS_PER_PAGE } from 'app/shared';

@Component({
    selector: 'jhi-carrier-ratings-custom',
    templateUrl: './nitro-rating.component.html'
})
export class NitroRatingCustomComponent extends NitroRatingComponent {

    @Input()
    query: any = {};

    @Input()
    itemsPerPage = ITEMS_PER_PAGE;

    filter() {
        const whitelist = this.query.whitelist;
        const blacklist = this.query.blacklist;

        if (whitelist) {
            this.ratings = this.ratings
                .filter(rating => rating.whiteList === true);
        } else if (blacklist) {
            this.ratings = this.ratings
                .filter(rating => rating.recommendation as Grade === Grade.BLACK_LIST);
        }
    }
}
