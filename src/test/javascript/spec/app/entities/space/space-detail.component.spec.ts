import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PruebasJHipsterTestModule } from '../../../test.module';
import { SpaceDetailComponent } from 'app/entities/space/space-detail.component';
import { Space } from 'app/shared/model/space.model';

describe('Component Tests', () => {
  describe('Space Management Detail Component', () => {
    let comp: SpaceDetailComponent;
    let fixture: ComponentFixture<SpaceDetailComponent>;
    const route = ({ data: of({ space: new Space(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [PruebasJHipsterTestModule],
        declarations: [SpaceDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(SpaceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SpaceDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load space on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.space).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
