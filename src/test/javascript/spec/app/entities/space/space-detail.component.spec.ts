import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';
import { JhiDataUtils } from 'ng-jhipster';

import { PruebasJHipsterTestModule } from '../../../test.module';
import { SpaceDetailComponent } from 'app/entities/space/space-detail.component';
import { Space } from 'app/shared/model/space.model';

describe('Component Tests', () => {
  describe('Space Management Detail Component', () => {
    let comp: SpaceDetailComponent;
    let fixture: ComponentFixture<SpaceDetailComponent>;
    let dataUtils: JhiDataUtils;
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
      dataUtils = fixture.debugElement.injector.get(JhiDataUtils);
    });

    describe('OnInit', () => {
      it('Should load space on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.space).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from JhiDataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeContentType, fakeBase64);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeContentType, fakeBase64);
      });
    });
  });
});
