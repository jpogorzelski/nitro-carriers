/* tslint:disable max-line-length */
import { getTestBed, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take } from 'rxjs/operators';
import { NitroRatingService } from 'app/entities/nitro-rating/nitro-rating.service';
import { CargoType, Grade, Rating } from 'app/shared/model/rating.model';
import { Carrier } from 'app/shared/model/carrier.model';
import { Country } from 'app/shared/model/country.model';
import { Person } from 'app/shared/model/person.model';
import { City } from 'app/shared/model/city.model';
import { User } from 'app/core';

describe('Service Tests', () => {
    describe('NitroRating Service', () => {
        let injector: TestBed;
        let service: NitroRatingService;
        let httpMock: HttpTestingController;
        let elemDefault: Rating;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(NitroRatingService);
            httpMock = injector.get(HttpTestingController);

            const carrier = new Carrier(0, 'Nitro sp zoo', 12312, [], []);
            const person = new Person(0, 'Jan', 'Kowalski');
            const country = new Country(0, 'Polska');
            const city = new City(0, 'Warszawa');
            elemDefault = new Rating(
                0,
                '11-111',
                '11-111',
                false,
                0,
                0,
                CargoType.EXTRA_REEFER,
                333.75,
                1,
                2,
                3,
                Grade.DEF_YES,
                3,
                'test remarks',
                true,
                carrier,
                person,
                country,
                city,
                country,
                city
            );
        });

        describe('Service methods', async () => {
            it('should find an element', async () => {
                const returnedFromService = Object.assign({}, elemDefault);
                service
                    .find(123)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: elemDefault }));

                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should create a NitroRating', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
                service
                    .create(new Rating(null))
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'POST' });
                req.flush(JSON.stringify(returnedFromService));
            });

            it('should update a NitroRating', async () => {
                const returnedFromService = Object.assign(
                    {
                        contact: 1,
                        price: 1,
                        flexibility: 1,
                        recommendation: 'BBBBBB',
                        average: 1
                    },
                    elemDefault
                );

                const expected = Object.assign({}, returnedFromService);
                service
                    .update(expected)
                    .pipe(take(1))
                    .subscribe(resp => expect(resp).toMatchObject({ body: expected }));
                const req = httpMock.expectOne({ method: 'PUT' });
                req.flush(JSON.stringify(returnedFromService));
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
