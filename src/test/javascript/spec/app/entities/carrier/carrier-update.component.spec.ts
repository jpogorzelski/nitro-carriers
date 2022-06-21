import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NitroCarriersTestModule } from '../../../test.module';
import { CarrierUpdateComponent } from 'app/entities/carrier/carrier-update.component';
import { CarrierService } from 'app/entities/carrier/carrier.service';
import { Carrier } from 'app/shared/model/carrier.model';

describe('Component Tests', () => {
    describe('Carrier Management Update Component', () => {
        let comp: CarrierUpdateComponent;
        let fixture: ComponentFixture<CarrierUpdateComponent>;
        let service: CarrierService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NitroCarriersTestModule],
                declarations: [CarrierUpdateComponent],
            })
                .overrideTemplate(CarrierUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CarrierUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CarrierService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Carrier(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.carrier = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Carrier();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.carrier = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
