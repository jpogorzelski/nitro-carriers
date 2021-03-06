import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Carrier } from 'app/shared/model/carrier.model';
import { IRating, Rating } from 'app/shared/model/rating.model';
import { NitroRatingService } from './nitro-rating.service';
import { NitroRatingEditorComponent } from './nitro-rating-editor.component';
import { NitroRatingDetailComponent } from 'app/entities/nitro-rating/nitro-rating-detail.component';
import { NitroRatingDeletePopupComponent } from './nitro-rating-delete-dialog.component';
import { NitroRatingListComponent } from 'app/entities/nitro-rating/nitro-rating-list.component';

@Injectable({ providedIn: 'root' })
export class NitroRatingResolve implements Resolve<IRating> {
    constructor(private service: NitroRatingService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<IRating> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Rating>) => response.ok),
                map((rating: HttpResponse<Rating>) => rating.body)
            );
        }
        const newRating = new Rating();
        newRating.carrier = new Carrier();
        return of(newRating);
    }
}

export const nitroRatingRoute: Routes = [
    {
        path: '',
        component: NitroRatingListComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nitroCarriersApp.rating.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/view',
        component: NitroRatingDetailComponent,
        resolve: {
            rating: NitroRatingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nitroCarriersApp.rating.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'new',
        component: NitroRatingEditorComponent,
        resolve: {
            rating: NitroRatingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nitroCarriersApp.rating.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/edit',
        component: NitroRatingEditorComponent,
        resolve: {
            rating: NitroRatingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nitroCarriersApp.rating.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: ':id/copy',
        component: NitroRatingEditorComponent,
        resolve: {
            rating: NitroRatingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nitroCarriersApp.rating.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const nitroRatingPopupRoute: Routes = [
    {
        path: ':id/delete',
        component: NitroRatingDeletePopupComponent,
        resolve: {
            rating: NitroRatingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nitroCarriersApp.rating.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
