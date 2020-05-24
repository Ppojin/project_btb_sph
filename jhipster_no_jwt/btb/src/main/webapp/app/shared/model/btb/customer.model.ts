import { Moment } from 'moment';
import { IUser } from 'app/shared/model/user.model';
import { Gender } from 'app/shared/model/enumerations/gender.model';
import { Role } from 'app/shared/model/enumerations/role.model';

export interface ICustomer {
  id?: number;
  idCustomer?: string;
  firstName?: string;
  lastName?: string;
  gender?: Gender;
  email?: string;
  phone?: string;
  city?: string;
  signinDate?: string;
  role?: Role;
  user?: IUser;
}

export const defaultValue: Readonly<ICustomer> = {};
