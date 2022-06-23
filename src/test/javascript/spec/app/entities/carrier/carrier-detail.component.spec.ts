/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NitroCarriersTestModule } from '../../../test.module';
import { CarrierDetailComponent } from 'app/entities/carrier/carrier-detail.component';
import { Carrier } from 'app/shared/model/carrier.model';

describe('Component Tests', () => {
    describe('Carrier Management Detail Component', () => {
        let comp: CarrierDetailComponent;
        let fixture: ComponentFixture<CarrierDetailComponent>;
        const route = ({ data: of({ carrier: new Carrier(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NitroCarriersTestModule],
                declarations: [CarrierDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CarrierDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CarrierDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.carrier).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
