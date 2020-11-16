import { Moment } from 'moment';
import { ISpace } from 'app/shared/model/space.model';
import { IUserm } from 'app/shared/model/userm.model';

export interface IUserInfo {
  id?: number;
  sex?: boolean;
  birthDate?: Moment;
  country?: string;
  town?: string;
  postCode?: string;
  spaces?: ISpace[];
  user?: IUserm;
}

export class UserInfo implements IUserInfo {
  constructor(
    public id?: number,
    public sex?: boolean,
    public birthDate?: Moment,
    public country?: string,
    public town?: string,
    public postCode?: string,
    public spaces?: ISpace[],
    public user?: IUserm
  ) {
    this.sex = this.sex || false;
  }
}
