import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, setFileData, openFile, byteSize, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, setBlob, reset } from './exam-result.reducer';
import { IExamResult } from 'app/shared/model/exam/exam-result.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IExamResultUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ExamResultUpdate = (props: IExamResultUpdateProps) => {
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { examResultEntity, loading, updating } = props;

  const { result, resultContentType } = examResultEntity;

  const handleClose = () => {
    props.history.push('/exam-result' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  const onBlobChange = (isAnImage, name) => event => {
    setFileData(event, (contentType, data) => props.setBlob(name, data, contentType), isAnImage);
  };

  const clearBlob = name => () => {
    props.setBlob(name, undefined, undefined);
  };

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.startDate = convertDateTimeToServer(values.startDate);
    values.endDate = convertDateTimeToServer(values.endDate);

    if (errors.length === 0) {
      const entity = {
        ...examResultEntity,
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
          <h2 id="btbApp.examExamResult.home.createOrEditLabel">
            <Translate contentKey="btbApp.examExamResult.home.createOrEditLabel">Create or edit a ExamResult</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : examResultEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="exam-result-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="exam-result-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="idExcerciseResultLabel" for="exam-result-idExcerciseResult">
                  <Translate contentKey="btbApp.examExamResult.idExcerciseResult">Id Excercise Result</Translate>
                </Label>
                <AvField
                  id="exam-result-idExcerciseResult"
                  type="text"
                  name="idExcerciseResult"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="scoreLabel" for="exam-result-score">
                  <Translate contentKey="btbApp.examExamResult.score">Score</Translate>
                </Label>
                <AvField id="exam-result-score" type="string" className="form-control" name="score" />
              </AvGroup>
              <AvGroup>
                <Label id="gitUrlLabel" for="exam-result-gitUrl">
                  <Translate contentKey="btbApp.examExamResult.gitUrl">Git Url</Translate>
                </Label>
                <AvField id="exam-result-gitUrl" type="text" name="gitUrl" />
              </AvGroup>
              <AvGroup>
                <Label id="startDateLabel" for="exam-result-startDate">
                  <Translate contentKey="btbApp.examExamResult.startDate">Start Date</Translate>
                </Label>
                <AvInput
                  id="exam-result-startDate"
                  type="datetime-local"
                  className="form-control"
                  name="startDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.examResultEntity.startDate)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="endDateLabel" for="exam-result-endDate">
                  <Translate contentKey="btbApp.examExamResult.endDate">End Date</Translate>
                </Label>
                <AvInput
                  id="exam-result-endDate"
                  type="datetime-local"
                  className="form-control"
                  name="endDate"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.examResultEntity.endDate)}
                />
              </AvGroup>
              <AvGroup>
                <AvGroup>
                  <Label id="resultLabel" for="result">
                    <Translate contentKey="btbApp.examExamResult.result">Result</Translate>
                  </Label>
                  <br />
                  {result ? (
                    <div>
                      {resultContentType ? (
                        <a onClick={openFile(resultContentType, result)}>
                          <Translate contentKey="entity.action.open">Open</Translate>
                        </a>
                      ) : null}
                      <br />
                      <Row>
                        <Col md="11">
                          <span>
                            {resultContentType}, {byteSize(result)}
                          </span>
                        </Col>
                        <Col md="1">
                          <Button color="danger" onClick={clearBlob('result')}>
                            <FontAwesomeIcon icon="times-circle" />
                          </Button>
                        </Col>
                      </Row>
                    </div>
                  ) : null}
                  <input id="file_result" type="file" onChange={onBlobChange(false, 'result')} />
                  <AvInput type="hidden" name="result" value={result} />
                </AvGroup>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/exam-result" replace color="info">
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
  examResultEntity: storeState.examResult.entity,
  loading: storeState.examResult.loading,
  updating: storeState.examResult.updating,
  updateSuccess: storeState.examResult.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  setBlob,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ExamResultUpdate);
