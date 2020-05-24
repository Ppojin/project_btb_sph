import { browser, element, by, protractor } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import ExamResultComponentsPage, { ExamResultDeleteDialog } from './exam-result.page-object';
import ExamResultUpdatePage from './exam-result-update.page-object';
import {
  waitUntilDisplayed,
  waitUntilAnyDisplayed,
  click,
  getRecordsCount,
  waitUntilHidden,
  waitUntilCount,
  isVisible,
} from '../../../util/utils';
import path from 'path';

const expect = chai.expect;

describe('ExamResult e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let examResultComponentsPage: ExamResultComponentsPage;
  let examResultUpdatePage: ExamResultUpdatePage;
  let examResultDeleteDialog: ExamResultDeleteDialog;
  const fileToUpload = '../../../../../../../src/main/webapp/content/images/logo-jhipster.png';
  const absolutePath = path.resolve(__dirname, fileToUpload);
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

  it('should load ExamResults', async () => {
    await navBarPage.getEntityPage('exam-result');
    examResultComponentsPage = new ExamResultComponentsPage();
    expect(await examResultComponentsPage.title.getText()).to.match(/Exam Results/);

    expect(await examResultComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilAnyDisplayed([examResultComponentsPage.noRecords, examResultComponentsPage.table]);

    beforeRecordsCount = (await isVisible(examResultComponentsPage.noRecords)) ? 0 : await getRecordsCount(examResultComponentsPage.table);
  });

  it('should load create ExamResult page', async () => {
    await examResultComponentsPage.createButton.click();
    examResultUpdatePage = new ExamResultUpdatePage();
    expect(await examResultUpdatePage.getPageTitle().getAttribute('id')).to.match(/btbApp.examExamResult.home.createOrEditLabel/);
    await examResultUpdatePage.cancel();
  });

  it('should create and save ExamResults', async () => {
    await examResultComponentsPage.createButton.click();
    await examResultUpdatePage.setIdExcerciseResultInput('64c99148-3908-465d-8c4a-e510e3ade974');
    expect(await examResultUpdatePage.getIdExcerciseResultInput()).to.match(/64c99148-3908-465d-8c4a-e510e3ade974/);
    await examResultUpdatePage.setScoreInput('5');
    expect(await examResultUpdatePage.getScoreInput()).to.eq('5');
    await examResultUpdatePage.setGitUrlInput('gitUrl');
    expect(await examResultUpdatePage.getGitUrlInput()).to.match(/gitUrl/);
    await examResultUpdatePage.setStartDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await examResultUpdatePage.getStartDateInput()).to.contain('2001-01-01T02:30');
    await examResultUpdatePage.setEndDateInput('01/01/2001' + protractor.Key.TAB + '02:30AM');
    expect(await examResultUpdatePage.getEndDateInput()).to.contain('2001-01-01T02:30');
    await examResultUpdatePage.setResultInput(absolutePath);
    await waitUntilDisplayed(examResultUpdatePage.saveButton);
    await examResultUpdatePage.save();
    await waitUntilHidden(examResultUpdatePage.saveButton);
    expect(await isVisible(examResultUpdatePage.saveButton)).to.be.false;

    expect(await examResultComponentsPage.createButton.isEnabled()).to.be.true;

    await waitUntilDisplayed(examResultComponentsPage.table);

    await waitUntilCount(examResultComponentsPage.records, beforeRecordsCount + 1);
    expect(await examResultComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);
  });

  it('should delete last ExamResult', async () => {
    const deleteButton = examResultComponentsPage.getDeleteButton(examResultComponentsPage.records.last());
    await click(deleteButton);

    examResultDeleteDialog = new ExamResultDeleteDialog();
    await waitUntilDisplayed(examResultDeleteDialog.deleteModal);
    expect(await examResultDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/btbApp.examExamResult.delete.question/);
    await examResultDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(examResultDeleteDialog.deleteModal);

    expect(await isVisible(examResultDeleteDialog.deleteModal)).to.be.false;

    await waitUntilAnyDisplayed([examResultComponentsPage.noRecords, examResultComponentsPage.table]);

    const afterCount = (await isVisible(examResultComponentsPage.noRecords)) ? 0 : await getRecordsCount(examResultComponentsPage.table);
    expect(afterCount).to.eq(beforeRecordsCount);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
