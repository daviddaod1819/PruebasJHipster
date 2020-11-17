import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PruebasJHipsterTestModule } from '../../../test.module';
import { SpaceUpdateComponent } from 'app/entities/space/space-update.component';
import { SpaceService } from 'app/entities/space/space.service';
import { Space } from 'app/shared/model/space.model';

describe('Component Tests', () => {
  describe('Space Management Update Component', () => {
    let comp: SpaceUpdateComponent;
    let fixture: ComponentFixture<SpaceUpdateComponent>;
    let service: SpaceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PruebasJHipsterTestModule],
        declarations: [SpaceUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SpaceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SpaceUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SpaceService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Space(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new Space();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
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
