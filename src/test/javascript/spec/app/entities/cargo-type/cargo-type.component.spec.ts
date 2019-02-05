/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { NitroCarriersTestModule } from '../../../test.module';
import { CargoTypeComponent } from 'app/entities/cargo-type/cargo-type.component';
import { CargoTypeService } from 'app/entities/cargo-type/cargo-type.service';
import { CargoType } from 'app/shared/model/cargo-type.model';

describe('Component Tests', () => {
    describe('CargoType Management Component', () => {
        let comp: CargoTypeComponent;
        let fixture: ComponentFixture<CargoTypeComponent>;
        let service: CargoTypeService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NitroCarriersTestModule],
                declarations: [CargoTypeComponent],
                providers: []
            })
                .overrideTemplate(CargoTypeComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(CargoTypeComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CargoTypeService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new CargoType(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.cargoTypes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
