import React from 'react'
import RegisterForm from './RegisterForm'

describe('<RegisterForm />', () => {
  it('renders', () => {
    // see: https://on.cypress.io/mounting-react
    cy.mount(<RegisterForm />)
  })
})