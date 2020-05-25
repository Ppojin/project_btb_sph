import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './exam-student.reducer';
import { IExamStudent } from 'app/shared/model/exam/exam-student.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IExamStudentProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ExamStudent = (props: IExamStudentProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { examStudentList, match, loading } = props;
  return (
    <div>
      <h2 id="exam-student-heading">
        <Translate contentKey="btbApp.examExamStudent.home.title">Exam Students</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="btbApp.examExamStudent.home.createLabel">Create new Exam Student</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {examStudentList && examStudentList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="btbApp.examExamStudent.idCustomer">Id Customer</Translate>
                </th>
                <th>
                  <Translate contentKey="btbApp.examExamStudent.exam">Exam</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {examStudentList.map((examStudent, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${examStudent.id}`} color="link" size="sm">
                      {examStudent.id}
                    </Button>
                  </td>
                  <td>{examStudent.idCustomer}</td>
                  <td>{examStudent.exam ? <Link to={`exam/${examStudent.exam.id}`}>{examStudent.exam.id}</Link> : ''}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${examStudent.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${examStudent.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${examStudent.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="btbApp.examExamStudent.home.notFound">No Exam Students found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ examStudent }: IRootState) => ({
  examStudentList: examStudent.entities,
  loading: examStudent.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ExamStudent);
