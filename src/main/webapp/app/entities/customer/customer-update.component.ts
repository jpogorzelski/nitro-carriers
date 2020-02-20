import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { ICustomer } from 'app/shared/model/customer.model';
import { CustomerService } from './customer.service';
import { City, ICity } from 'app/shared/model/city.model';
import { CityService } from 'app/entities/city';
import { ICountry } from 'app/shared/model/country.model';
import { CountryService } from 'app/entities/country';
import { IUser, UserService } from 'app/core';

@Component({
    selector: 'jhi-customer-update',
    templateUrl: './customer-update.component.html'
})
export class CustomerUpdateComponent implements OnInit {
    customer: ICustomer;
    isSaving: boolean;

    cities: ICity[];

    countries: ICountry[];

    users: IUser[];

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected customerService: CustomerService,
        protected cityService: CityService,
        protected countryService: CountryService,
        protected userService: UserService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ customer }) => {
            this.customer = customer;
        });
        this.cityService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICity[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICity[]>) => response.body)
            )
            .subscribe((res: ICity[]) => (this.cities = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.countryService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<ICountry[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICountry[]>) => response.body)
            )
            .subscribe((res: ICountry[]) => (this.countries = res), (res: HttpErrorResponse) => this.onError(res.message));
        this.userService
            .query()
            .pipe(
                filter((mayBeOk: HttpResponse<IUser[]>) => mayBeOk.ok),
                map((response: HttpResponse<IUser[]>) => response.body)
            )
            .subscribe((res: IUser[]) => (this.users = res), (res: HttpErrorResponse) => this.onError(res.message));
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.customer.id !== undefined) {
            this.subscribeToSaveResponse(this.customerService.update(this.customer));
        } else {
            this.subscribeToSaveResponse(this.customerService.create(this.customer));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<ICustomer>>) {
        result.subscribe((res: HttpResponse<ICustomer>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackCityById(index: number, item: ICity) {
        return item.id;
    }

    trackCountryById(index: number, item: ICountry) {
        return item.id;
    }

    trackUserById(index: number, item: IUser) {
        return item.id;
    }

    addCity(cityName: string) {
        return Object.assign(new City(), { cityName });
    }

    onCountrySelect(country: ICountry) {
        if (country) {
            console.table('### Selected country: ', country.countryNamePL);
            this.cityService
                .search({
                    query: country.countryNamePL
                })
                .pipe(
                    filter((mayBeOk: HttpResponse<ICity[]>) => mayBeOk.ok),
                    map((response: HttpResponse<ICity[]>) => response.body)
                )
                .subscribe(
                    (res: ICity[]) => {
                        this.cities = res;
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
        }
    }
}
