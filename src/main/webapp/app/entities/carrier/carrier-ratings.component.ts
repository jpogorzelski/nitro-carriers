import { Component } from '@angular/core';
import { RatingComponent } from 'app/entities/rating/rating.component';

@Component({
    selector: 'jhi-carrier-ratings',
     templateUrl: '../nitro-rating/nitro-rating.component.html'
})
export class CarrierRatingsComponent extends RatingComponent {
    searchEnabled = false;
    headerEnabled = false;
}
