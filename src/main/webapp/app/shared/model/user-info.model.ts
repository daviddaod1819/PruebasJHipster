import { Moment } from 'moment';
import { ISpace } from 'app/shared/model/space.model';

export interface IUserInfo {
  id?: number;
  sex?: boolean;
  birthDate?: Moment;
  country?: string;
  town?: string;
  postCode?: string;
  spaces?: ISpace[];
}

export class UserInfo implements IUserInfo {
  constructor(
    public id?: number,
    public sex?: boolean,
    public birthDate?: Moment,
    public country?: string,
    public town?: string,
    public postCode?: string,
    public spaces?: ISpace[]
  ) {
    this.sex = this.sex || false;
  }
}
