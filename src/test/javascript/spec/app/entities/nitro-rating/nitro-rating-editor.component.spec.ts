/* tslint:disable max-line-length */
import {ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';
import {HttpResponse} from '@angular/common/http';
import {of} from 'rxjs';

import {NitroCarriersTestModule} from '../../../test.module';
import {NitroRatingEditorComponent} from 'app/entities/nitro-rating/nitro-rating-editor.component';
import {NitroRatingService} from 'app/entities/nitro-rating/nitro-rating.service';
import {NitroRating} from 'app/shared/model/nitro-rating.model';

describe('Component Tests', () => {
    describe('NitroRating Management Update Component', () => {
        let comp: NitroRatingEditorComponent;
        let fixture: ComponentFixture<NitroRatingEditorComponent>;
        let service: NitroRatingService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NitroCarriersTestModule],
                declarations: [NitroRatingEditorComponent]
            })
                .overrideTemplate(NitroRatingEditorComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(NitroRatingEditorComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NitroRatingService);
        });

        describe("NitroRating mapping text to obj", () => {
            it('Should map correctly', fakeAsync(() => {
                let src = 'DK Spółka Z Ograniczoną Odpowiedzialnością\n' +
                    'Stara Iwiczna, Nowa 17/1, 05-500 Piaseczno, PL\n' +
                    'Anna Gotlib, 915298-1\n' +
                    'tel. 577902102';
                let result = comp.extractCarrierAndPerson(new NitroRating(), src);
                expect(result.carrierName).toEqual('DK Spółka Z Ograniczoną Odpowiedzialnością');
                expect(result.carrierTransId).toEqual(915298);
                expect(result.personFirstName).toEqual('Anna');
                expect(result.personLastName).toEqual('Gotlib');
                expect(result.personTransId).toEqual(1);
            }));
        });
        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new NitroRating(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({body: entity})));
                comp.rating = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new NitroRating();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({body: entity})));
                comp.rating = entity;
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
