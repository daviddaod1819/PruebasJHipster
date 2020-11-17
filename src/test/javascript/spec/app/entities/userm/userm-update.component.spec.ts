import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { PruebasJHipsterTestModule } from '../../../test.module';
import { UsermUpdateComponent } from 'app/entities/userm/userm-update.component';
import { UsermService } from 'app/entities/userm/userm.service';
import { Userm } from 'app/shared/model/userm.model';

describe('Component Tests', () => {
  describe('Userm Management Update Component', () => {
    let comp: UsermUpdateComponent;
    let fixture: ComponentFixture<UsermUpdateComponent>;
    let service: UsermService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PruebasJHipsterTestModule],
        declarations: [UsermUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(UsermUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(UsermUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(UsermService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new Userm(123);
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
        const entity = new Userm();
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
