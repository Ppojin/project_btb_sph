import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IExam } from 'app/shared/model/exam/exam.model';
import { getEntities as getExams } from 'app/entities/exam/exam/exam.reducer';
import { getEntity, updateEntity, createEntity, reset } from './exam-student.reducer';
import { IExamStudent } from 'app/shared/model/exam/exam-student.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IExamStudentUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ExamStudentUpdate = (props: IExamStudentUpdateProps) => {
  const [examId, setExamId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { examStudentEntity, exams, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/exam-student');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getExams();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...examStudentEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="btbApp.examExamStudent.home.createOrEditLabel">
            <Translate contentKey="btbApp.examExamStudent.home.createOrEditLabel">Create or edit a ExamStudent</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : examStudentEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="exam-student-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="exam-student-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="idCustomerLabel" for="exam-student-idCustomer">
                  <Translate contentKey="btbApp.examExamStudent.idCustomer">Id Customer</Translate>
                </Label>
                <AvField id="exam-student-idCustomer" type="text" name="idCustomer" />
              </AvGroup>
              <AvGroup>
                <Label for="exam-student-exam">
                  <Translate contentKey="btbApp.examExamStudent.exam">Exam</Translate>
                </Label>
                <AvInput id="exam-student-exam" type="select" className="form-control" name="exam.id">
                  <option value="" key="0" />
                  {exams
                    ? exams.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/exam-student" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  exams: storeState.exam.entities,
  examStudentEntity: storeState.examStudent.entity,
  loading: storeState.examStudent.loading,
  updating: storeState.examStudent.updating,
  updateSuccess: storeState.examStudent.updateSuccess,
});

const mapDispatchToProps = {
  getExams,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ExamStudentUpdate);
