import { Component, DoCheck, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { EMPTY, Observable, Observer } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { JhiAlertService } from 'ng-jhipster';
import { Grade, IRating } from 'app/shared/model/rating.model';
import { NitroRatingService } from './nitro-rating.service';
import { IPerson, Person } from 'app/shared/model/person.model';
import { Carrier, ICarrier } from 'app/shared/model/carrier.model';
import { ICountry } from 'app/shared/model/country.model';
import { CountryService } from 'app/entities/country';
import { City, ICity } from 'app/shared/model/city.model';
import { CityService } from 'app/entities/city';

@Component({
    selector: 'jhi-ext-rating-update',
    templateUrl: './nitro-rating-editor.component.html'
})
export class NitroRatingEditorComponent implements OnInit, DoCheck {
    carrierAndPerson: string;
    altCarrierAndPerson: string;
    rating: IRating;
    isSaving: boolean;

    people: IPerson[];
    countries: ICountry[];
    chargeCities: ICity[] = [];
    dischargeCities: ICity[] = [];
    carriers: ICarrier[];
    negativeGrade = false;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected ratingExtService: NitroRatingService,
        protected countryService: CountryService,
        protected cityService: CityService,
        protected activatedRoute: ActivatedRoute,
        private router: Router
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({rating}) => {
            this.rating = rating;
            if (this.rating.id) {
                this.carrierAndPerson = this.joinCarrierAndPersonData(this.rating.carrier, this.rating.person);
                if (this.rating.addAlternative) {
                    this.altCarrierAndPerson = this.joinCarrierAndPersonData(this.rating.altCarrier, this.rating.altPerson);
                }
            }
        });

        this.countryService
            .query({ filter: 'rating-is-null' })
            .pipe(
                filter((mayBeOk: HttpResponse<ICountry[]>) => mayBeOk.ok),
                map((response: HttpResponse<ICountry[]>) => response.body)
            )
            .subscribe(
                (res: ICountry[]) => {
                    this.countries = res;
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        this.onChargeCountrySelect(this.rating.chargeCountry);
        this.onDischargeCountrySelect(this.rating.dischargeCountry);

        console.table('on init:', this.rating);
    }

    private joinCarrierAndPersonData(carrier: ICarrier, person: IPerson) {
        return carrier.name +
            '\n' +
            person.firstName +
            ' ' +
            person.lastName +
            ', ' +
            carrier.transId +
            '-' +
            person.companyId;
    }

    ngDoCheck(): void {
        if (this.rating.contact && this.rating.price && this.rating.flexibility && this.rating.recommendation) {
            const recommendation = this.getRecommendationAsInt();
            this.rating.average = (this.rating.contact + this.rating.price + this.rating.flexibility + recommendation) / 4;
        }
        if (this.rating.totalPrice && this.rating.distance) {
            const exactPricePerKm = this.rating.totalPrice / this.rating.distance;
            this.rating.pricePerKm = Number(exactPricePerKm.toFixed(2));
        }

        // white list stuff
        if ([Grade.BLACK_LIST as Grade, Grade.DEF_NO as Grade, Grade.NO as Grade].includes(this.rating.recommendation as Grade)) {
            this.negativeGrade = true;
            this.rating.whiteList = false;
        } else {
            this.negativeGrade = false;
        }
    }

    extractCarrierAndPerson(rating: IRating, str: string): IRating {
        const result = JSON.parse(JSON.stringify(rating));
        const lines = str.split('\n');
        const personLineIndex = getPersonLineIndex();
        const nameAndTransId = lines[personLineIndex].split(',');
        const fullName = nameAndTransId[0].split(' ');
        const transId = nameAndTransId[1].split('-');
        result.carrier = new Carrier();
        result.carrier.name = lines[0];
        result.carrier.transId = Number(transId[0]);

        result.person = new Person();
        result.person.firstName = fullName[0];
        result.person.lastName = fullName[1];
        result.person.companyId = Number(transId[1]);
        return result;

        function getPersonLineIndex() {
            switch (lines.length) {
                case 2: // only company name line and person with transId line
                    return 1;
                case 3: // missing telephone line or address line
                    if (lines[2].startsWith('tel')) {
                        return 1;
                    } else {
                        return 2;
                    }
                case 4: // full data
                    return 2;
                default:
                    throw new Error('Bad input');
            }
        }
    }

    private getRecommendationAsInt() {
        let recommendation = 0;
        switch (this.rating.recommendation) {
            case Grade.BLACK_LIST:
                recommendation = 1;
                break;
            case Grade.DEF_NO:
                recommendation = 2;
                break;
            case Grade.NO:
                recommendation = 3;
                break;
            case Grade.FINE:
                recommendation = 4;
                break;
            case Grade.YES:
                recommendation = 5;
                break;
            case Grade.DEF_YES:
                recommendation = 6;
                break;
            default:
                break;
        }
        return recommendation;
    }

    previousState() {
        window.history.back();
    }

    save() {
        let extractedRating = this.extractCarrierAndPerson(this.rating, this.carrierAndPerson);
        this.rating.carrier = extractedRating.carrier;
        this.rating.person = extractedRating.person;

        if (this.rating.addAlternative) {
            extractedRating = this.extractCarrierAndPerson(this.rating, this.altCarrierAndPerson);
            this.rating.altCarrier = extractedRating.carrier;
            this.rating.altPerson = extractedRating.person;
        }

        this.isSaving = true;
        if (this.rating.id !== undefined) {
            this.subscribeToSaveResponse(this.ratingExtService.update(this.rating));
        } else {
            this.subscribeToSaveResponse(this.ratingExtService.create(this.rating));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IRating>>) {
        result.subscribe((res: HttpResponse<IRating>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackCountryById(index: number, item: ICountry) {
        return item.id;
    }

    trackCityById(index: number, item: ICity) {
        return item.id;
    }

    addCity(cityName: string) {
        return Object.assign(new City(), {cityName});
    }

    onChargeCountrySelect(country: ICountry) {
        this.onCountrySelectInternal(country).subscribe(res => {
            this.chargeCities = res;
        });
    }

    onDischargeCountrySelect(country: ICountry) {
        this.onCountrySelectInternal(country).subscribe(res => {
            this.dischargeCities = res;
        });
    }

    onCountrySelectInternal(country: ICountry): Observable<ICity[]> {
        const cities: ICity[] = [];
        if (country) {
            console.table('### Selected country: ', country.countryNamePL);
            return Observable.create((observer: Observer<ICity[]>) => {
                this.cityService
                    .search({
                        query: country.countryNamePL
                    })
                    .pipe(
                        filter((mayBeOk: HttpResponse<ICity[]>) => mayBeOk.ok),
                        map((response: HttpResponse<ICity[]>) => response.body)
                    )
                    .subscribe((res: ICity[]) => {
                        console.table('Downloaded cities: ', cities);
                        observer.next(res);
                        observer.complete();
                    }, (res: HttpErrorResponse) => this.onError(res.message));
            });
        }
        return EMPTY;

    }

    copyFromBasic() {
        this.altCarrierAndPerson = this.carrierAndPerson;
    }
}
