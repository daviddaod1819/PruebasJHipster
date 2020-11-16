import { IUserInfo } from 'app/shared/model/user-info.model';

export interface IUserm {
  id?: number;
  userInfo?: IUserInfo;
}

export class Userm implements IUserm {
  constructor(public id?: number, public userInfo?: IUserInfo) {}
}
