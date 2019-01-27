import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { INitroRating, NitroRating } from 'app/shared/model/nitro-rating.model';
import { NitroRatingService } from './nitro-rating.service';
import { NitroRatingEditorComponent } from './nitro-rating-editor.component';

@Injectable({ providedIn: 'root' })
export class NitroRatingResolve implements Resolve<INitroRating> {
    constructor(private service: NitroRatingService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<INitroRating> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<NitroRating>) => response.ok),
                map((rating: HttpResponse<NitroRating>) => rating.body)
            );
        }
        return of(new NitroRating());
    }
}

export const nitroRatingRoute: Routes = [
    {
        path: '',
        component: NitroRatingEditorComponent,
        resolve: {
            rating: NitroRatingResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nitroCarriersApp.nitroRating.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];
