import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import ExamStudentComponentsPage, { ExamStudentDeleteDialog } from './exam-student.page-object';
import ExamStudentUpdatePage from './exam-student-update.page-object';
import {
  waitUntilDisplayed,
  waitUntilAnyDisplayed,
  click,
  getRecordsCount,
  waitUntilHidden,
  waitUntilCount,
  isVisible,
} from '../../../util/utils';

const expect = chai.expect;

describe('ExamStudent e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let examStudentComponentsPage: ExamStudentComponentsPage;
  let examStudentUpdatePage: ExamStudentUpdatePage;
  let examStudentDeleteDialog: ExamStudentDeleteDialog;
  let beforeRecordsCount = 0;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.waitUntilDisplayed();

    await signInPage.username.sendKeys('admin');
    await signInPage.password.sendKeys('admin');
    await signInPage.loginButton.click();
    await signInPage.waitUntilHidden();
    await waitUntilDisplayed(navBarPage.entityMenu);
    await waitUntilDisplayed(navBarPage.adminMenu);
    await waitUntilDisplayed(navBarPage.accountMenu);
  });

  it('should load ExamStudents', async () => {
    await navBarPage.getEntityPage('exam-student');
    examStudentComponentsPage = new ExamStudentComponentsPage();
    expect(await examStudentComponentsPage.title.getText()).to.match(/Exam Students/);

    expect(await examStudentComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilAnyDisplayed([examStudentComponentsPage.noRecords, examStudentComponentsPage.table]);

    beforeRecordsCount = (await isVisible(examStudentComponentsPage.noRecords))
      ? 0
      : await getRecordsCount(examStudentComponentsPage.table);
  });

  it('should load create ExamStudent page', async () => {
    await examStudentComponentsPage.createButton.click();
    examStudentUpdatePage = new ExamStudentUpdatePage();
    expect(await examStudentUpdatePage.getPageTitle().getAttribute('id')).to.match(/btbApp.examExamStudent.home.createOrEditLabel/);
    await examStudentUpdatePage.cancel();
  });

  it('should create and save ExamStudents', async () => {
    await examStudentComponentsPage.createButton.click();
    await examStudentUpdatePage.setIdCustomerInput('64c99148-3908-465d-8c4a-e510e3ade974');
    expect(await examStudentUpdatePage.getIdCustomerInput()).to.match(/64c99148-3908-465d-8c4a-e510e3ade974/);
    await examStudentUpdatePage.examSelectLastOption();
    await waitUntilDisplayed(examStudentUpdatePage.saveButton);
    await examStudentUpdatePage.save();
    await waitUntilHidden(examStudentUpdatePage.saveButton);
    expect(await isVisible(examStudentUpdatePage.saveButton)).to.be.false;

    expect(await examStudentComponentsPage.createButton.isEnabled()).to.be.true;

    await waitUntilDisplayed(examStudentComponentsPage.table);

    await waitUntilCount(examStudentComponentsPage.records, beforeRecordsCount + 1);
    expect(await examStudentComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);
  });

  it('should delete last ExamStudent', async () => {
    const deleteButton = examStudentComponentsPage.getDeleteButton(examStudentComponentsPage.records.last());
    await click(deleteButton);

    examStudentDeleteDialog = new ExamStudentDeleteDialog();
    await waitUntilDisplayed(examStudentDeleteDialog.deleteModal);
    expect(await examStudentDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/btbApp.examExamStudent.delete.question/);
    await examStudentDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(examStudentDeleteDialog.deleteModal);

    expect(await isVisible(examStudentDeleteDialog.deleteModal)).to.be.false;

    await waitUntilAnyDisplayed([examStudentComponentsPage.noRecords, examStudentComponentsPage.table]);

    const afterCount = (await isVisible(examStudentComponentsPage.noRecords)) ? 0 : await getRecordsCount(examStudentComponentsPage.table);
    expect(afterCount).to.eq(beforeRecordsCount);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
