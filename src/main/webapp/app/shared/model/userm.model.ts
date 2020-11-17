export interface IUserm {
  id?: number;
  userInfoId?: number;
}

export class Userm implements IUserm {
  constructor(public id?: number, public userInfoId?: number) {}
}
