import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import ExamQABankComponentsPage, { ExamQABankDeleteDialog } from './exam-qa-bank.page-object';
import ExamQABankUpdatePage from './exam-qa-bank-update.page-object';
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

describe('ExamQABank e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let examQABankComponentsPage: ExamQABankComponentsPage;
  let examQABankUpdatePage: ExamQABankUpdatePage;
  let examQABankDeleteDialog: ExamQABankDeleteDialog;
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

  it('should load ExamQABanks', async () => {
    await navBarPage.getEntityPage('exam-qa-bank');
    examQABankComponentsPage = new ExamQABankComponentsPage();
    expect(await examQABankComponentsPage.title.getText()).to.match(/Exam QA Banks/);

    expect(await examQABankComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilAnyDisplayed([examQABankComponentsPage.noRecords, examQABankComponentsPage.table]);

    beforeRecordsCount = (await isVisible(examQABankComponentsPage.noRecords)) ? 0 : await getRecordsCount(examQABankComponentsPage.table);
  });

  it('should load create ExamQABank page', async () => {
    await examQABankComponentsPage.createButton.click();
    examQABankUpdatePage = new ExamQABankUpdatePage();
    expect(await examQABankUpdatePage.getPageTitle().getAttribute('id')).to.match(/btbApp.examExamQaBank.home.createOrEditLabel/);
    await examQABankUpdatePage.cancel();
  });

  it('should create and save ExamQABanks', async () => {
    await examQABankComponentsPage.createButton.click();
    await examQABankUpdatePage.setIdQABankInput('64c99148-3908-465d-8c4a-e510e3ade974');
    expect(await examQABankUpdatePage.getIdQABankInput()).to.match(/64c99148-3908-465d-8c4a-e510e3ade974/);
    await waitUntilDisplayed(examQABankUpdatePage.saveButton);
    await examQABankUpdatePage.save();
    await waitUntilHidden(examQABankUpdatePage.saveButton);
    expect(await isVisible(examQABankUpdatePage.saveButton)).to.be.false;

    expect(await examQABankComponentsPage.createButton.isEnabled()).to.be.true;

    await waitUntilDisplayed(examQABankComponentsPage.table);

    await waitUntilCount(examQABankComponentsPage.records, beforeRecordsCount + 1);
    expect(await examQABankComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);
  });

  it('should delete last ExamQABank', async () => {
    const deleteButton = examQABankComponentsPage.getDeleteButton(examQABankComponentsPage.records.last());
    await click(deleteButton);

    examQABankDeleteDialog = new ExamQABankDeleteDialog();
    await waitUntilDisplayed(examQABankDeleteDialog.deleteModal);
    expect(await examQABankDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/btbApp.examExamQaBank.delete.question/);
    await examQABankDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(examQABankDeleteDialog.deleteModal);

    expect(await isVisible(examQABankDeleteDialog.deleteModal)).to.be.false;

    await waitUntilAnyDisplayed([examQABankComponentsPage.noRecords, examQABankComponentsPage.table]);

    const afterCount = (await isVisible(examQABankComponentsPage.noRecords)) ? 0 : await getRecordsCount(examQABankComponentsPage.table);
    expect(afterCount).to.eq(beforeRecordsCount);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
