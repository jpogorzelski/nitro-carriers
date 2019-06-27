import { Component, Input } from '@angular/core';
import { RatingComponent } from 'app/entities/rating/rating.component';
import { RatingService } from 'app/entities/rating';
import { AccountService } from 'app/core';
import { ActivatedRoute } from '@angular/router';
import { JhiParseLinks, JhiEventManager, JhiAlertService } from 'ng-jhipster';
import { CarrierService } from 'app/entities/carrier/carrier.service';
import { IRating } from 'app/shared/model/rating.model';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

@Component({
    selector: 'jhi-carrier-ratings',
     templateUrl: '../nitro-rating/nitro-rating.component.html'
})
export class CarrierRatingsComponent extends RatingComponent {
    searchEnabled = false;
    headerEnabled = false;

    @Input()
    id: number;

    constructor(
        protected ratingService: RatingService,
        protected jhiAlertService: JhiAlertService,
        protected eventManager: JhiEventManager,
        protected parseLinks: JhiParseLinks,
        protected activatedRoute: ActivatedRoute,
        protected accountService: AccountService,
        protected carrierService: CarrierService
    ) {
        super(ratingService, jhiAlertService, eventManager, parseLinks, activatedRoute, accountService);
    }

    loadRatings() {
        this.carrierService
            .findRatings(this.id)
            .subscribe(
                (res: HttpResponse<IRating[]>) => this.paginateRatings(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
    }
}
