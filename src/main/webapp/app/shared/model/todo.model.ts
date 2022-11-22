import { ICustomer } from '@/shared/model/customer.model';
import { ITask } from '@/shared/model/task.model';

export interface ITodo {
  id?: number;
  createdAt?: Date | null;
  customers?: ICustomer[] | null;
  tasks?: ITask[] | null;
}

export class Todo implements ITodo {
  constructor(public id?: number, public createdAt?: Date | null, public customers?: ICustomer[] | null, public tasks?: ITask[] | null) {}
}
