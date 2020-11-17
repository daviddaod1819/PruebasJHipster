import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PruebasJHipsterTestModule } from '../../../test.module';
import { UsermDetailComponent } from 'app/entities/userm/userm-detail.component';
import { Userm } from 'app/shared/model/userm.model';

describe('Component Tests', () => {
  describe('Userm Management Detail Component', () => {
    let comp: UsermDetailComponent;
    let fixture: ComponentFixture<UsermDetailComponent>;
    const route = ({ data: of({ userm: new Userm(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PruebasJHipsterTestModule],
        declarations: [UsermDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(UsermDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(UsermDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load userm on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.userm).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
