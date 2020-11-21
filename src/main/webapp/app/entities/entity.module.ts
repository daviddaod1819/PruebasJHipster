import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'space',
        loadChildren: () => import('./space/space.module').then(m => m.PruebasJHipsterSpaceModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class PruebasJHipsterEntityModule {}
