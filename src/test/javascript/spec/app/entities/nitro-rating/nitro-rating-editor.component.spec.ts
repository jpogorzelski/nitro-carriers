import { ComponentFixture, fakeAsync, TestBed, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { of } from 'rxjs';

import { NitroCarriersTestModule } from '../../../test.module';
import { NitroRatingEditorComponent } from 'app/entities/nitro-rating/nitro-rating-editor.component';
import { NitroRatingService } from 'app/entities/nitro-rating/nitro-rating.service';
import { Rating } from 'app/shared/model/rating.model';

describe('Component Tests', () => {
    describe('NitroRating Management Update Component', () => {
        let comp: NitroRatingEditorComponent;
        let fixture: ComponentFixture<NitroRatingEditorComponent>;
        let service: NitroRatingService;
        const src =
            'DK Spółka Z Ograniczoną Odpowiedzialnością\n' +
            'Stara Iwiczna, Nowa 17/1, 05-500 Piaseczno, PL\n' +
            'Anna Gotlib, 915298-1\n' +
            'tel. 577902102';
        const srcWithoutTel =
            'DK Spółka Z Ograniczoną Odpowiedzialnością\n' + 'Stara Iwiczna, Nowa 17/1, 05-500 Piaseczno, PL\n' + 'Anna Gotlib, 915298-1';
        const srcWithoutAddress = 'DK Spółka Z Ograniczoną Odpowiedzialnością\n' + 'Anna Gotlib, 915298-1\n' + 'tel. 577902102';
        const srcWithoutTelAndAddress = 'DK Spółka Z Ograniczoną Odpowiedzialnością\n' + 'Anna Gotlib, 915298-1';

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [NitroCarriersTestModule],
                declarations: [NitroRatingEditorComponent],
            })
                .overrideTemplate(NitroRatingEditorComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(NitroRatingEditorComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(NitroRatingService);
        });

        function verifyMappedFields(srcText) {
            const result = comp.extractCarrierAndPerson(new Rating(), srcText);
            expect(result.carrier.name).toEqual('DK Spółka Z Ograniczoną Odpowiedzialnością');
            expect(result.carrier.transId).toEqual(915298);
            expect(result.person.firstName).toEqual('Anna');
            expect(result.person.lastName).toEqual('Gotlib');
            expect(result.person.companyId).toEqual(1);
            return result;
        }

        describe('NitroRating mapping text to obj', () => {
            it('Should map correctly full', fakeAsync(() => {
                const result = verifyMappedFields(src);
                expect(result.person.phoneNumber).toEqual('577902102');
            }));
            it('Should map correctly without tel', fakeAsync(() => {
                const result = verifyMappedFields(srcWithoutTel);
                expect(result.person.phoneNumber).toBeUndefined();
            }));
            it('Should map correctly without address', fakeAsync(() => {
                const result = verifyMappedFields(srcWithoutAddress);
                expect(result.person.phoneNumber).toEqual('577902102');
            }));
            it('Should map correctly without tel and address', fakeAsync(() => {
                const result = verifyMappedFields(srcWithoutTelAndAddress);
                expect(result.person.phoneNumber).toBeUndefined();
            }));
        });
        describe('save', () => {
            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Rating();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.rating = entity;
                comp.carrierAndPerson = src;
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
