import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICargoType } from 'app/shared/model/cargo-type.model';
import { CargoTypeService } from './cargo-type.service';

@Component({
    selector: 'jhi-cargo-type-delete-dialog',
    templateUrl: './cargo-type-delete-dialog.component.html'
})
export class CargoTypeDeleteDialogComponent {
    cargoType: ICargoType;

    constructor(
        protected cargoTypeService: CargoTypeService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.cargoTypeService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'cargoTypeListModification',
                content: 'Deleted an cargoType'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-cargo-type-delete-popup',
    template: ''
})
export class CargoTypeDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ cargoType }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CargoTypeDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.cargoType = cargoType;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/cargo-type', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/cargo-type', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
