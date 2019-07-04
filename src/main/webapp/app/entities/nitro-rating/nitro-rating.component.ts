import { RatingComponent } from 'app/entities/rating';
import { Component } from '@angular/core';
import { IRating } from 'app/shared/model/rating.model';

@Component({
    selector: 'jhi-nitro-rating',
    templateUrl: './nitro-rating.component.html'
})
export class NitroRatingComponent extends RatingComponent {
    printPerson(rating: IRating): string {
        return (
            `${rating.person.firstName} ${rating.person.lastName}<br/>` +
            `tel. ${rating.person.phoneNumber}<br />` +
            `${rating.carrier.transId}-${rating.person.companyId}`
        );
    }
}
