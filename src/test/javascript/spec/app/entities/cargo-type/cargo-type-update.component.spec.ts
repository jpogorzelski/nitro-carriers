/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { NitroCarriersTestModule } from '../../../test.module';
import { CargoTypeUpdateComponent } from 'app/entities/cargo-type/cargo-type-update.component';
import { CargoTypeService } from 'app/entities/cargo-type/cargo-type.service';
import { CargoType } from 'app/shared/model/cargo-type.model';

describe('Component Tests', () => {
    describe('CargoType Management Update Component', () => {
        let comp: CargoTypeUpdateComponent;
        let fixture: ComponentFixture<CargoTypeUpdateComponent>;
        let service: CargoTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NitroCarriersTestModule],
                declarations: [CargoTypeUpdateComponent]
            })
                .overrideTemplate(CargoTypeUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CargoTypeUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CargoTypeService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new CargoType(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.cargoType = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new CargoType();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.cargoType = entity;
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
