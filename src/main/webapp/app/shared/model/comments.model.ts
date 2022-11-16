import { ITask } from '@/shared/model/task.model';

export interface IComments {
  id?: number;
  message?: string | null;
  task?: ITask | null;
}

export class Comments implements IComments {
  constructor(public id?: number, public message?: string | null, public task?: ITask | null) {}
}
