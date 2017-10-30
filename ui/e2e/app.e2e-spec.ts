import { ServiceEzyUiPage } from './app.po';

describe('service-ezy-ui App', function() {
  let page: ServiceEzyUiPage;

  beforeEach(() => {
    page = new ServiceEzyUiPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
