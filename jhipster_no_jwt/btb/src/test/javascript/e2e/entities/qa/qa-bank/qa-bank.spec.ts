import { browser, element, by } from 'protractor';

import NavBarPage from './../../../page-objects/navbar-page';
import SignInPage from './../../../page-objects/signin-page';
import QABankComponentsPage, { QABankDeleteDialog } from './qa-bank.page-object';
import QABankUpdatePage from './qa-bank-update.page-object';
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

describe('QABank e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let qABankComponentsPage: QABankComponentsPage;
  let qABankUpdatePage: QABankUpdatePage;
  let qABankDeleteDialog: QABankDeleteDialog;
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

  it('should load QABanks', async () => {
    await navBarPage.getEntityPage('qa-bank');
    qABankComponentsPage = new QABankComponentsPage();
    expect(await qABankComponentsPage.title.getText()).to.match(/QA Banks/);

    expect(await qABankComponentsPage.createButton.isEnabled()).to.be.true;
    await waitUntilAnyDisplayed([qABankComponentsPage.noRecords, qABankComponentsPage.table]);

    beforeRecordsCount = (await isVisible(qABankComponentsPage.noRecords)) ? 0 : await getRecordsCount(qABankComponentsPage.table);
  });

  it('should load create QABank page', async () => {
    await qABankComponentsPage.createButton.click();
    qABankUpdatePage = new QABankUpdatePage();
    expect(await qABankUpdatePage.getPageTitle().getAttribute('id')).to.match(/btbApp.qaQABank.home.createOrEditLabel/);
    await qABankUpdatePage.cancel();
  });

  it('should create and save QABanks', async () => {
    await qABankComponentsPage.createButton.click();
    await qABankUpdatePage.setIdQABankInput('64c99148-3908-465d-8c4a-e510e3ade974');
    expect(await qABankUpdatePage.getIdQABankInput()).to.match(/64c99148-3908-465d-8c4a-e510e3ade974/);
    await qABankUpdatePage.setTitleInput('title');
    expect(await qABankUpdatePage.getTitleInput()).to.match(/title/);
    await qABankUpdatePage.setContentsInput('contents');
    expect(await qABankUpdatePage.getContentsInput()).to.match(/contents/);
    await qABankUpdatePage.setGitUrlInput('gitUrl');
    expect(await qABankUpdatePage.getGitUrlInput()).to.match(/gitUrl/);
    await qABankUpdatePage.categorySelectLastOption();
    await waitUntilDisplayed(qABankUpdatePage.saveButton);
    await qABankUpdatePage.save();
    await waitUntilHidden(qABankUpdatePage.saveButton);
    expect(await isVisible(qABankUpdatePage.saveButton)).to.be.false;

    expect(await qABankComponentsPage.createButton.isEnabled()).to.be.true;

    await waitUntilDisplayed(qABankComponentsPage.table);

    await waitUntilCount(qABankComponentsPage.records, beforeRecordsCount + 1);
    expect(await qABankComponentsPage.records.count()).to.eq(beforeRecordsCount + 1);
  });

  it('should delete last QABank', async () => {
    const deleteButton = qABankComponentsPage.getDeleteButton(qABankComponentsPage.records.last());
    await click(deleteButton);

    qABankDeleteDialog = new QABankDeleteDialog();
    await waitUntilDisplayed(qABankDeleteDialog.deleteModal);
    expect(await qABankDeleteDialog.getDialogTitle().getAttribute('id')).to.match(/btbApp.qaQABank.delete.question/);
    await qABankDeleteDialog.clickOnConfirmButton();

    await waitUntilHidden(qABankDeleteDialog.deleteModal);

    expect(await isVisible(qABankDeleteDialog.deleteModal)).to.be.false;

    await waitUntilAnyDisplayed([qABankComponentsPage.noRecords, qABankComponentsPage.table]);

    const afterCount = (await isVisible(qABankComponentsPage.noRecords)) ? 0 : await getRecordsCount(qABankComponentsPage.table);
    expect(afterCount).to.eq(beforeRecordsCount);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
