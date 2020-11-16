import { IUserInfo } from 'app/shared/model/user-info.model';

export interface ISpace {
  id?: number;
  rooms?: number;
  meters?: number;
  price?: number;
  details?: string;
  userInfo?: IUserInfo;
}

export class Space implements ISpace {
  constructor(
    public id?: number,
    public rooms?: number,
    public meters?: number,
    public price?: number,
    public details?: string,
    public userInfo?: IUserInfo
  ) {}
}
