import { IUser } from '@/shared/model/user.model';
import { IGroups } from '@/shared/model/groups.model';

export interface ICustomer {
  id?: number;
  user?: IUser | null;
  group?: IGroups | null;
}

export class Customer implements ICustomer {
  constructor(public id?: number, public user?: IUser | null, public group?: IGroups | null) {}
}
