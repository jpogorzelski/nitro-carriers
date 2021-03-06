import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ICarrier } from 'app/shared/model/carrier.model';
import { CarrierService } from './carrier.service';

@Component({
    selector: 'jhi-carrier-delete-dialog',
    templateUrl: './carrier-delete-dialog.component.html'
})
export class CarrierDeleteDialogComponent {
    carrier: ICarrier;

    constructor(protected carrierService: CarrierService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.carrierService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'carrierListModification',
                content: 'Deleted an carrier'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-carrier-delete-popup',
    template: ''
})
export class CarrierDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ carrier }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(CarrierDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.carrier = carrier;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate(['/carrier', { outlets: { popup: null } }]);
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate(['/carrier', { outlets: { popup: null } }]);
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
