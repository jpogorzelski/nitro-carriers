import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { CargoType } from 'app/shared/model/cargo-type.model';
import { CargoTypeService } from './cargo-type.service';
import { CargoTypeComponent } from './cargo-type.component';
import { CargoTypeDetailComponent } from './cargo-type-detail.component';
import { CargoTypeUpdateComponent } from './cargo-type-update.component';
import { CargoTypeDeletePopupComponent } from './cargo-type-delete-dialog.component';
import { ICargoType } from 'app/shared/model/cargo-type.model';

@Injectable({ providedIn: 'root' })
export class CargoTypeResolve implements Resolve<ICargoType> {
    constructor(private service: CargoTypeService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<CargoType> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<CargoType>) => response.ok),
                map((cargoType: HttpResponse<CargoType>) => cargoType.body)
            );
        }
        return of(new CargoType());
    }
}

export const cargoTypeRoute: Routes = [
    {
        path: 'cargo-type',
        component: CargoTypeComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nitroCarriersApp.cargoType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cargo-type/:id/view',
        component: CargoTypeDetailComponent,
        resolve: {
            cargoType: CargoTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nitroCarriersApp.cargoType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cargo-type/new',
        component: CargoTypeUpdateComponent,
        resolve: {
            cargoType: CargoTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nitroCarriersApp.cargoType.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'cargo-type/:id/edit',
        component: CargoTypeUpdateComponent,
        resolve: {
            cargoType: CargoTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nitroCarriersApp.cargoType.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const cargoTypePopupRoute: Routes = [
    {
        path: 'cargo-type/:id/delete',
        component: CargoTypeDeletePopupComponent,
        resolve: {
            cargoType: CargoTypeResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'nitroCarriersApp.cargoType.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
