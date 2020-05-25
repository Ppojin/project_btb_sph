import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { Translate, translate } from 'react-jhipster';
import { NavLink as Link } from 'react-router-dom';
import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown
    icon="th-list"
    name={translate('global.menu.entities.main')}
    id="entity-menu"
    style={{ maxHeight: '80vh', overflow: 'auto' }}
  >
    <MenuItem icon="asterisk" to="/customer">
      <Translate contentKey="global.menu.entities.btbCustomer" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/qa-bank">
      <Translate contentKey="global.menu.entities.qaQaBank" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/exam">
      <Translate contentKey="global.menu.entities.examExam" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/exam-qa-bank">
      <Translate contentKey="global.menu.entities.examExamQaBank" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/exam-student">
      <Translate contentKey="global.menu.entities.examExamStudent" />
    </MenuItem>
    <MenuItem icon="asterisk" to="/exam-result">
      <Translate contentKey="global.menu.entities.examExamResult" />
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
