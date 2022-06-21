import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { NitroCarriersTestModule } from '../../../test.module';
import { CarrierComponent } from 'app/entities/carrier/carrier.component';
import { CarrierService } from 'app/entities/carrier/carrier.service';
import { Carrier } from 'app/shared/model/carrier.model';

describe('Component Tests', () => {
    describe('Carrier Management Component', () => {
        let comp: CarrierComponent;
        let fixture: ComponentFixture<CarrierComponent>;
        let service: CarrierService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NitroCarriersTestModule],
                declarations: [CarrierComponent],
                providers: [],
            })
                .overrideTemplate(CarrierComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CarrierComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CarrierService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Carrier(123)],
                        headers,
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.carriers[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
