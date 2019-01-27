/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { NitroCarriersTestModule } from '../../../test.module';
import { CargoTypeDetailComponent } from 'app/entities/cargo-type/cargo-type-detail.component';
import { CargoType } from 'app/shared/model/cargo-type.model';

describe('Component Tests', () => {
    describe('CargoType Management Detail Component', () => {
        let comp: CargoTypeDetailComponent;
        let fixture: ComponentFixture<CargoTypeDetailComponent>;
        const route = ({ data: of({ cargoType: new CargoType(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NitroCarriersTestModule],
                declarations: [CargoTypeDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(CargoTypeDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(CargoTypeDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.cargoType).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
