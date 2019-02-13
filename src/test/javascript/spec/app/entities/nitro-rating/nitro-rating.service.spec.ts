/* tslint:disable max-line-length */
import { getTestBed, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { take } from 'rxjs/operators';
import { NitroRatingService } from 'app/entities/nitro-rating/nitro-rating.service';
import { Grade, INitroRating, NitroRating } from 'app/shared/model/nitro-rating.model';

describe('Service Tests', () => {
    describe('NitroRating Service', () => {
        let injector: TestBed;
        let service: NitroRatingService;
        let httpMock: HttpTestingController;
        let elemDefault: INitroRating;
        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [HttpClientTestingModule]
            });
            injector = getTestBed();
            service = injector.get(NitroRatingService);
            httpMock = injector.get(HttpTestingController);

            elemDefault = new NitroRating(
                0,
                0,
                'default',
                0,
                'first',
                'last',
                'country',
                '11-111',
                'country',
                '11-111',
                'plandeka',
                0,
                333.75,
                1,
                1,
                1,
                Grade.DEF_YES,
                1
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
                    .create(new NitroRating(null))
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
