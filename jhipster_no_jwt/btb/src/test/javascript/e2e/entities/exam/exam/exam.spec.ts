import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import ExamComponentsPage, { ExamDeleteDialog } from './exam.page-object';
import ExamUpdatePage from './exam-update.page-object';
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

describe('Exam e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let examComponentsPage: ExamComponentsPage;
  let examUpdatePage: ExamUpdatePage;
  let examDeleteDialog: ExamDeleteDialog;
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

  it('should load Exams', async () => {
    await navBarPage.getEntityPage('exam');
    examComponentsPage = new ExamComponentsPage();
    expect(await examComponentsPage.title.getText()).to.match(/Exams/);

    expect(await examComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilAnyDisplayed([examComponentsPage.noRecords, examComponentsPage.table]);

    beforeRecordsCount = (await isVisible(examComponentsPage.noRecords)) ? 0 : await getRecordsCount(examComponentsPage.table);
  });

  it('should load create Exam page', async () => {
    await examComponentsPage.createButton.click();
    examUpdatePage = new ExamUpdatePage();
    expect(await examUpdatePage.getPageTitle().getAttribute('id')).to.match(/btbApp.examExam.home.createOrEditLabel/);
    await examUpdatePage.cancel();
  });

  it('should create and save Exams', async () => {
    await examComponentsPage.createButton.click();
    await examUpdatePage.setIdExamInput('64c99148-3908-465d-8c4a-e510e3ade974');
    expect(await examUpdatePage.getIdExamInput()).to.match(/64c99148-3908-465d-8c4a-e510e3ade974/);
    await examUpdatePage.setTitleInput('title');
    expect(await examUpdatePage.getTitleInput()).to.match(/title/);
    await examUpdatePage.levelSelectLastOption();
    await examUpdatePage.setStartDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await examUpdatePage.getStartDateInput()).to.contain('2001-01-01T02:30');
    await examUpdatePage.setEndDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await examUpdatePage.getEndDateInput()).to.contain('2001-01-01T02:30');
    // examUpdatePage.examQABankSelectLastOption();
    await waitUntilDisplayed(examUpdatePage.saveButton);
    await examUpdatePage.save();
    await waitUntilHidden(examUpdatePage.saveButton);
    expect(await isVisible(examUpdatePage.saveButton)).to.be.false;

    expect(await examComponentsPage.createButton.isEnabled()).to.be.true;

    await waitUntilDisplayed(examComponentsPage.table);

    await waitUntilCount(examComponentsPage.records, beforeRecordsCount + 1);
    expect(await examComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);
  });

  it('should delete last Exam', async () => {
    const deleteButton = examComponentsPage.getDeleteButton(examComponentsPage.records.last());
    await click(deleteButton);

    examDeleteDialog = new ExamDeleteDialog();
    await waitUntilDisplayed(examDeleteDialog.deleteModal);
    expect(await examDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/btbApp.examExam.delete.question/);
    await examDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(examDeleteDialog.deleteModal);

    expect(await isVisible(examDeleteDialog.deleteModal)).to.be.false;

    await waitUntilAnyDisplayed([examComponentsPage.noRecords, examComponentsPage.table]);

    const afterCount = (await isVisible(examComponentsPage.noRecords)) ? 0 : await getRecordsCount(examComponentsPage.table);
    expect(afterCount).to.eq(beforeRecordsCount);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
