import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';
import { take, map } from 'rxjs/operators';
import { RatingService } from 'app/entities/rating/rating.service';
import { IRating, Rating, CargoType, Grade } from 'app/shared/model/rating.model';

describe('Service Tests', () => {
    describe('Rating Service', () => {
        let injector: TestBed;
        let service: RatingService;
        let httpMock: HttpTestingController;
        let elemDefault: IRating;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule],
            });
            injector = getTestBed();
            service = injector.get(RatingService);
            httpMock = injector.get(HttpTestingController);

            elemDefault = new Rating(
                0,
                'AAAAAAA',
                'AAAAAAA',
                false,
                0,
                0,
                CargoType.FTL_13_6,
                0,
                0,
                0,
                0,
                Grade.DEF_YES,
                0,
                'AAAAAAA',
                false
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

            it('should create a Rating', async () => {
                const returnedFromService = Object.assign(
                    {
                        id: 0,
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

            it('should update a Rating', async () => {
                const returnedFromService = Object.assign(
                    {
                        chargePostalCode: 'BBBBBB',
                        dischargePostalCode: 'BBBBBB',
                        addAlternative: true,
                        totalPrice: 1,
                        pricePerKm: 1,
                        cargoType: 'BBBBBB',
                        distance: 1,
                        contact: 1,
                        price: 1,
                        flexibility: 1,
                        recommendation: 'BBBBBB',
                        average: 1,
                        remarks: 'BBBBBB',
                        whiteList: true,
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

            it('should return a list of Rating', async () => {
                const returnedFromService = Object.assign(
                    {
                        chargePostalCode: 'BBBBBB',
                        dischargePostalCode: 'BBBBBB',
                        addAlternative: true,
                        totalPrice: 1,
                        pricePerKm: 1,
                        cargoType: 'BBBBBB',
                        distance: 1,
                        contact: 1,
                        price: 1,
                        flexibility: 1,
                        recommendation: 'BBBBBB',
                        average: 1,
                        remarks: 'BBBBBB',
                        whiteList: true,
                    },
                    elemDefault
                );
                const expected = Object.assign({}, returnedFromService);
                service
                    .query(expected)
                    .pipe(
                        take(1),
                        map(resp => resp.body)
                    )
                    .subscribe(body => expect(body).toContainEqual(expected));
                const req = httpMock.expectOne({ method: 'GET' });
                req.flush(JSON.stringify([returnedFromService]));
                httpMock.verify();
            });

            it('should delete a Rating', async () => {
                const rxPromise = service.delete(123).subscribe(resp => expect(resp.ok));

                const req = httpMock.expectOne({ method: 'DELETE' });
                req.flush({ status: 200 });
            });
        });

        afterEach(() => {
            httpMock.verify();
        });
    });
});
