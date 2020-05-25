import { Moment } from 'moment';
import { IExamStudent } from 'app/shared/model/exam/exam-student.model';
import { IExamQABank } from 'app/shared/model/exam/exam-qa-bank.model';
import { Level } from 'app/shared/model/enumerations/level.model';

export interface IExam {
  id?: number;
  idExam?: string;
  title?: string;
  level?: Level;
  startDate?: string;
  endDate?: string;
  examStudents?: IExamStudent[];
  examQABanks?: IExamQABank[];
}

export const defaultValue: Readonly<IExam> = {};
