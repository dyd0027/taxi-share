describe('Register Page', () => {
    it('should submit the registration form', () => {
      cy.visit('/signup');
      cy.get('input[name="userId"]').type('testuser');
      cy.get('input[name="userPassword"]').type('password123');
      cy.get('input[name="userName"]').type('John Doe');
      cy.get('input[name="phoneNum"]').type('010-1234-5678');
      cy.get('select[name="userSex"]').select('M');
      cy.get('button[type="submit"]').click();
    });
  });
  