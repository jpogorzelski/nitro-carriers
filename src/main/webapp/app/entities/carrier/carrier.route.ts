import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Carrier } from 'app/shared/model/carrier.model';
import { CarrierService } from './carrier.service';
import { CarrierComponent } from './carrier.component';
import { CarrierDetailComponent } from './carrier-detail.component';
import { CarrierUpdateComponent } from './carrier-update.component';
import { CarrierDeletePopupComponent } from './carrier-delete-dialog.component';
import { ICarrier } from 'app/shared/model/carrier.model';

@Injectable({ providedIn: 'root' })
export class CarrierResolve implements Resolve<ICarrier> {
    constructor(private service: CarrierService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Carrier> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Carrier>) => response.ok),
                map((carrier: HttpResponse<Carrier>) => carrier.body)
            );
        }
        return of(new Carrier());
    }
}

export const carrierRoute: Routes = [
    {
        path: 'carrier',
        component: CarrierComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nitroCarriersApp.carrier.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'carrier/:id/view',
        component: CarrierDetailComponent,
        resolve: {
            carrier: CarrierResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nitroCarriersApp.carrier.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'carrier/new',
        component: CarrierUpdateComponent,
        resolve: {
            carrier: CarrierResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nitroCarriersApp.carrier.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'carrier/:id/edit',
        component: CarrierUpdateComponent,
        resolve: {
            carrier: CarrierResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nitroCarriersApp.carrier.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const carrierPopupRoute: Routes = [
    {
        path: 'carrier/:id/delete',
        component: CarrierDeletePopupComponent,
        resolve: {
            carrier: CarrierResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nitroCarriersApp.carrier.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
